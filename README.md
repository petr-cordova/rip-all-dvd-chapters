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

# Notes
I wrote and used this script to rip A Beka DVDs.

Is it legal?
It's not legal to copy DVDs for the purpose of redistribution.
However, as far as I can see - it's legal for the purpose of "fair use":

In the 2009 case [RealNetworks v. DVD CCA](https://www.eff.org/cases/realnetworks-v-dvd-cca-realdvd-case), the [final injunction](https://www.eff.org/files/filenode/RealDVD/real_v_dvd-cca_pi_order_081109.pdf) reads, "while it may well be fair use for an individual consumer to store a backup copy of a personally owned DVD on that individual's computer, a federal law has nonetheless made it illegal to manufacture or traffic in a device or tool that permits a consumer to make such copies."
This case made clear that manufacturing and distribution of circumvention tools was illegal, but use of those tools for non-infringing purposes, including fair use purposes, was not.

Use on your own risk.
