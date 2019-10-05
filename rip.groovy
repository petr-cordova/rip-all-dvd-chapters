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

def json = out.toString()
String marker = 'JSON Title Set:'
json = json.substring(json.indexOf(marker) + marker.length())
Map dvd_data = new JsonSlurper().parseText(json)

def name = dvd_data.TitleList.Name[0].toString()
println "${LocalTime.now()} DVD Name: $name"
new File("$saveTo$name").mkdir()

def chapterList = dvd_data.TitleList.ChapterList[0]
int chaptersCount = chapterList.size
for (int i=0; i<chaptersCount; i++) {
    int duration = chapterList[i].Duration.Hours * 60 * 60 +
                   chapterList[i].Duration.Minutes * 60 +
                   chapterList[i].Duration.Seconds
    print "${LocalTime.now()} ${chapterList[i].Name} out of ${chaptersCount}: $duration seconds... "

    if (duration < 3) {
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
String done = "\n${LocalTime.now()} Completed ripping in ${ChronoUnit.MINUTES.between(startTime, LocalTime.now())} minutes\n"
print done
logFile << done

// Eject DVD
String cmd = 'powershell (New-Object -com "WMPlayer.OCX.7").cdromcollection.item(0).eject()'
process = cmd.execute()
