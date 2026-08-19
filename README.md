# Logisim
Patch for the original Logisim (2010 build) to fix Dock "reopen" on modern macOS — self-contained .app with bundled Java, no separate JDK install required.


# Logisim macOS Dock Fix

A patch for the classic [Logisim](http://www.cburch.com/logisim/) (original 2010 build,
authored by Carl Burch) that fixes its behavior on modern versions of macOS.

## Problem

The original `.app`, built for macOS in the early 2010s, relied on the legacy
`JavaApplicationLauncher` system framework, which has since been removed from modern
macOS — the app wouldn't launch at all.

After rebuilding it with `jpackage` using a modern JDK, the app launched fine, but had a
second issue: after closing the window with the red close button, clicking the Dock icon
did nothing — the window would not reappear (only Cmd+Q worked correctly).

## Root cause

The code relied on the legacy Apple API `com.apple.eawt.*`, which has been removed from
modern Java. The check for its presence (`Class.forName("com.apple.eawt.Application")`)
always threw a `ClassNotFoundException`, so the macOS event handlers were never registered
in the first place.

## What was fixed

- `Startup.java` — removed the dependency on the deleted Apple API, replaced with a simple
  OS check
- `MacOsAdapter.java` — added a handler for the "reopen" event via the modern
  `java.awt.Desktop` API (available since Java 9), which brings the app's windows back to
  front when the Dock icon is clicked

## Building

Requires JDK 14+ (for `jpackage`). See `build.sh`.

## License

The original Logisim is licensed under the GNU General Public License (GPLv2 or, at your
option, any later version). This patch is distributed under the same terms.
Original project by Carl Burch: http://www.cburch.com/logisim/
