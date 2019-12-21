import groovy.json.JsonSlurper
import groovy.transform.SourceURI
import java.nio.file.Paths
import java.time.LocalTime
import java.time.temporal.ChronoUnit

// change these 2 lines below to your DVD drive and path for videos
String ripFrom = 'E:'
String saveTo = 'C:\\videos\\'

@SourceURI URI sourceURI
def scriptFolder = new File (Paths.get(sourceURI).parent.toUri())
def logFile = new File (scriptFolder, 'log.txt')

def startTime = LocalTime.now()
println "$startTime scanning DVD..."
def process = "HandBrakeCLI -i $ripFrom -t 0 --json".execute()
def out = new StringBuffer()
def err = new StringBuffer()
process.consumeProcessOutput(out, err)
process.waitFor()
logFile.write "$startTime\n$out\n"
logFile << err

String json = out.toString()
String marker = 'JSON Title Set:'
int markerPosition = json.indexOf(marker)
if (markerPosition == -1) {
    println "\nCan`t read DVD`s data. Is it inserted in the drive $ripFrom?\n"
    println err
    return 1
}
json = json.substring(markerPosition + marker.length())
Map dvd_data = new JsonSlurper().parseText(json)

def name = dvd_data.TitleList.Name[0].toString()
println "${LocalTime.now()} DVD Name: $name"
// possible renaming:
// name = name.replace('03 L ', '3 Day ')
// println "Will save to $name"
new File("$saveTo$name").mkdir()

def chapterList = dvd_data.TitleList.ChapterList[0]
int chaptersCount = chapterList.size
for (int i=0; i<chaptersCount; i++) {
    int duration = chapterList[i].Duration.Hours * 60 * 60 +
                   chapterList[i].Duration.Minutes * 60 +
                   chapterList[i].Duration.Seconds
    print "${LocalTime.now()} ${chapterList[i].Name} out of ${chaptersCount}: $duration seconds... "

    if (duration < 4) {
        println 'skipping'
    } else {
        String cmd = "HandBrakeCLI -i $ripFrom -t 1 --chapters ${i+1} --output \"$saveTo$name\\${chapterList[i].Name}.mp4\""
        println "\n\t$cmd"
        logFile << "\n\n${LocalTime.now()} $cmd\n"
        process = cmd.execute()
        out = new StringBuffer()
        err = new StringBuffer()
        process.consumeProcessOutput(out, err)
        process.waitFor()
        logFile << out
        logFile << err
    }
}
int duration = ChronoUnit.MINUTES.between(startTime, LocalTime.now())
if (duration < 0) {
   duration = 1440 + duration
}
String done = "\n${LocalTime.now()} Completed ripping in $duration minutes\n"
print done
logFile << done

// Eject DVD
String cmd = 'powershell (New-Object -com "WMPlayer.OCX.7").cdromcollection.item(0).eject()'
process = cmd.execute()
