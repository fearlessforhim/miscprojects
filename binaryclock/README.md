This README does not explain how to set up dependencies on your Raspberry Pi. Thankfully the binary clock is so simple the dependencies should be included with the OS.

**Quick start**
Running the clock is as simple as
python binary_clock.py

**Detailed Startup**
I have my Pi set up to start the clock when I plug it in. The included autostart bash script may be used but I believe it is unecessary. Whatever the case you'll need to add the following command to the end of your .bashrc file (in your home directory):
python binary_clock.py &

--IF USING AUTOSTART SCRIPT--
Make your autostart script executable with
chmod 755 autostart

Then add the following to the end of your .bashrc file
/home/pi/autostart &

The ampersand will free up your console to enter other commands rather than allow the binary clock to keep a hold on the console.