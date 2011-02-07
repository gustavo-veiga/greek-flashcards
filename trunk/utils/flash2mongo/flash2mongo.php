<?php

/**
 * This script processes .flash files into a mongo collection
 * Usage:
 * php flash2mongo.php [source flash dir] [mongo host] [mongo db] [ mongo collection]
 * 
 * mongodb://sworduser:swordpass@dbh04.mongolab.com:27047/sword
 */
function utf8_to_unicode_code($utf8_string) {
    $expanded = iconv("UTF-8", "UTF-32", $utf8_string);
    return unpack("L*", $expanded);
}

function unicode_code_to_utf8($unicode_list) {
    $result = "";
    foreach ($unicode_list as $key => $value) {
        $one_character = pack("L", $value);
        $result .= iconv("UTF-32", "UTF-8", $one_character);
    }
    return $result;
}

function convertUnicode($str) {
    $s = preg_replace('/\\\u(.{4})/i', '&#x$1;', $str);
    return html_entity_decode($s, ENT_NOQUOTES, 'UTF-8');
}

if ($argc < 3) {
    die("Need to supply directory of flash cards");
}

$lessonDir			= $argv[1];
$mongo_host			= $argv[2];
$mongo_db			= $argv[3];

if (!is_dir($lessonDir)) {
    die("Directory [" . $lessonDir . "] is invalid");
}

echo "Source directory: [" . $lessonDir . "]" . PHP_EOL;
echo "Mongo host: [" . $mongo_host . "]" . PHP_EOL;
echo "Mongo db: [" . $mongo_db . "]" . PHP_EOL;

$dirArray = array();

$myDirectory = opendir($argv[1]);

$mongo 		= new Mongo($mongo_host);
$db    		= $mongo->selectDB($mongo_db);

$db->dropCollection('lessonsets');
$db->dropCollection('lessons');
$db->dropCollection('cards');

$lessonsets_coll	= $db->selectCollection('lessonsets');
$lessons_coll 		= $db->selectCollection('lessons');
$cards_coll 		= $db->selectCollection('cards');

$lessonsets_coll->ensureIndex(array("name"=>1));

$lessons_coll->ensureIndex(array("name"=>1));

$cards_coll->ensureIndex(array("lessonset"=>1, "lesson"=>1));
$cards_coll->ensureIndex(array("front"=>1));
$cards_coll->ensureIndex(array("back"=>1));

while ($entryName = readdir($myDirectory)) {
    if (!preg_match('/^\./', $entryName) && is_dir($lessonDir . '/' . $entryName)) {
        $dirArray[] = $entryName;

        $lessonsets_coll->insert(array("name"=>$entryName));
    }
}

$dbh = null;
closedir($myDirectory);

echo "Found " . count($dirArray) . " lessonsets: " . json_encode($dirArray) . PHP_EOL;

foreach($dirArray as $dir) {
    $pdata = array();

    $lessonSetDir = $dir;

    $lessons = array();
    $myDirectory = opendir($lessonDir . '/' . $lessonSetDir);

    $lid = 1;
    $totwords = 0;
    while ($cardfile = readdir($myDirectory)) {
        if (!preg_match('/^\./', $entryName) && is_file($lessonDir . '/' . $lessonSetDir . '/' . $cardfile)) {
            $lessons[] = $cardfile;
            $lesson_data = @parse_ini_file($lessonDir . '/' . $lessonSetDir . '/' . $cardfile, false, INI_SCANNER_RAW);

            if (!is_array($lesson_data)) {
                echo $lessonDir . '/' . $lessonSetDir . '/' . $cardfile . ' failed to parse' . PHP_EOL;
                continue;
            }

            $lesson = preg_replace('/\.flash/i', '', $cardfile);

            if(!array_key_exists('lessonFont', $lesson_data)) {
                echo $lessonDir . '/' . $lessonSetDir . '/' . $cardfile . ' has no font specified ' . PHP_EOL;
            }

            $cardCount = $lesson_data['wordCount'];
            
            $lessonfont = array_key_exists('lessonFont', $lesson_data) ? $lesson_data['lessonFont'] : "GalSILB201";
            
            $lessons_coll->insert(array(
   					"lessonset"=>$lessonSetDir,
            		"name"=>$lesson,
					"font"=>$lessonfont
            ));
            
            $totwords += $cardCount;
            for ($i = 0; $i < $cardCount; $i++) {
                $front = convertUnicode($lesson_data['word' . $i]);
                $back = $lesson_data['answers' . $i];
				$cards_coll->insert(array(
					"lessonset"=>$lessonSetDir,
					"lesson"=>$lesson,
					"front"=>$front,
					"back"=>$back
				));
            }

            $lid++;
            echo $lessonDir . '/' . $lessonSetDir . '/' . $cardfile . ': processed ' . $cardCount . ' words' . PHP_EOL;
        }
    }

    echo 'processed ' . $lid . ' lessons and ' . $totwords . ' words' . PHP_EOL;

    closedir($myDirectory);
}
