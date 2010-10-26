<?php

/**
 * This script processes .flash files into corresponding SQLite .db files.
 * Usage:
 * php flash2sqlite.php [source flash dir] [target db dir]
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

$lessonDir = $argv[1];
$destDir = $argv[2];

if (!is_dir($lessonDir)) {
    die("Directory [" . $lessonDir . "] is invalid");
}

if (!is_dir($destDir)) {
    mkdir($destDir);
}

echo "Source directory: [" . $lessonDir . "]" . PHP_EOL;
echo "Destination directory: [" . $destDir . "]" . PHP_EOL;

$dirArray = array();

$myDirectory = opendir($argv[1]);

$dbFile = $destDir . '/lessons.db';
if (file_exists($dbFile))
    unlink($dbFile);

try {
    $dbh = new PDO('sqlite:' . $dbFile);
} catch (PDOException $e) {
    echo $e->getMessage();
    die();
}

$dbh->exec('CREATE TABLE IF NOT EXISTS android_metadata ("locale" TEXT DEFAULT "en_US")');
$dbh->exec('INSERT INTO android_metadata VALUES ("en_US")');

$dbh->exec('CREATE TABLE IF NOT EXISTS lesson_set (_id INTEGER PRIMARY KEY, name VARCHAR(255))');

while ($entryName = readdir($myDirectory)) {
    if (!preg_match('/^\./', $entryName) && is_dir($lessonDir . '/' . $entryName)) {
        $dirArray[] = $entryName;

        $dbh->exec('INSERT INTO lesson_set VALUES (null, "' . $entryName . '")');
    }
}

$dbh = null;
closedir($myDirectory);

echo "Found " . count($dirArray) . " lessonsets: " . json_encode($dirArray) . PHP_EOL;

$pids = array();

$parent_pid = getmypid();

for ($i = 0; $i < count($dirArray); $i++) {
    if (getmypid() == $parent_pid) {
        $pids[] = pcntl_fork();
    }
}

if (getmypid() == $parent_pid) {
    while (count($pids) > 0) {
        $pid = pcntl_waitpid(-1, $status);

        foreach ($pids as $key => $tpid) {
            if ($pid == $tpid)
                unset($pids[$key]);
        }
    }

    echo "All children exited" . PHP_EOL;
    $pids = array();
} else {
    $pdata = array();

    $lessonSetDir = $dirArray[count($pids) - 1];

    $dbFile = $destDir . '/' . $lessonSetDir . '.db';
    if (file_exists($dbFile))
        unlink($dbFile);

    try {
        $dbh = new PDO('sqlite:' . $dbFile);
    } catch (PDOException $e) {
        echo $e->getMessage();
        die();
    }

    $dbh->exec('CREATE TABLE IF NOT EXISTS android_metadata ("locale" TEXT DEFAULT "en_US")');
    $dbh->exec('INSERT INTO android_metadata VALUES ("en_US")');

    $dbh->exec('CREATE TABLE IF NOT EXISTS lesson (_id INTEGER PRIMARY KEY, name VARCHAR(255), font VARCHAR(255))');
    $dbh->exec('CREATE TABLE IF NOT EXISTS card (_id INTEGER PRIMARY KEY, lesson_id INTEGER, front TEXT, back TEXT)');

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

            if (array_key_exists('lessonFont', $lesson_data)) {
                $dbh->exec('INSERT INTO lesson VALUES (null, "' . $lesson . '", "' . $lesson_data['lessonFont'] . '")');
            } else {
                echo $lessonDir . '/' . $lessonSetDir . '/' . $cardfile . ' has no font specified ' . PHP_EOL;
            }

            $cardCount = $lesson_data['wordCount'];
            $totwords += $cardCount;
            for ($i = 0; $i < $cardCount; $i++) {
                $front = convertUnicode($lesson_data['word' . $i]);
                $back = $lesson_data['answers' . $i];

                $dbh->exec('INSERT INTO card VALUES (null, ' . $lid . ', "' . $front . '", "' . $back . '")');
            }

            $lid++;
            echo $lessonDir . '/' . $lessonSetDir . '/' . $cardfile . ': processed ' . $cardCount . ' words' . PHP_EOL;
        }
    }

    echo $dbFile . ': processed ' . $lid . ' lessons and ' . $totwords . ' words' . PHP_EOL;

    closedir($myDirectory);

    exit(0);
}