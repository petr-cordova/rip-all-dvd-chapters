# rip-multiply-chapters-dvd
Groovy script to rip DVD, which has multiply chapters

It depends on:
- Groovy: `choco install groovy`
- Java: `choco install jdk8`
- HandBrakeCLI: https://handbrake.fr/downloads2.php (tested with 1.2.2)

If you want to rip encrypted DVD, you will also need to download libdvdcss-2.dll:  
https://download.videolan.org/pub/libdvdcss/1.2.12/  
And copy it to HandBrake's folder.

Download `rip.groovy`.
Good idea to put it in the same folder, as HandBrakeCLI.
Edit lines 8 and 9 of it to point to your DVD drive and path to save videos.

Script assumes, that HandBrakeCLI is on the system PATH or in the current folder. So, either add path to HandBrakeCLI to system PATH, edit script to specify full path or have it all in the current folder.

# Run
`groovy ./rip.groovy`

Script tested on Windows 7 and 10.  
To run on Mac / Linux - modify to use correct path & devices on your system
