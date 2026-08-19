
#!/bin/bash
set -e

# ==== EDIT THESE PATHS FOR YOUR SYSTEM ====
JDK_BIN="$HOME/Desktop/jdk-25.0.4.1.jdk/Contents/Home/bin"
ORIGINAL_JAR="$HOME/Desktop/logisim-build/logisim.jar"
ICON="$HOME/Desktop/logisim-build/LogisimApp.icns"
# ===========================================

echo "1. Compiling patches..."
mkdir -p build
"$JDK_BIN/javac" -cp "$ORIGINAL_JAR" -d build patches/*.java

echo "2. Inserting patched classes into the jar..."
cd build
"$JDK_BIN/jar" uf "$ORIGINAL_JAR" \
  $(find com -name "MacOsAdapter*.class" -o -name "Startup*.class")
cd ..

echo "3. Building the .app with jpackage..."
JAR_DIR=$(dirname "$ORIGINAL_JAR")
JAR_NAME=$(basename "$ORIGINAL_JAR")
"$JDK_BIN/jpackage" \
  --input "$JAR_DIR" \
  --name Logisim \
  --main-jar "$JAR_NAME" \
  --main-class com.cburch.logisim.Main \
  --icon "$ICON" \
  --type app-image \
  --mac-package-name Logisim \
  --dest "$HOME/Desktop"

echo "Done! Logisim.app has been built on the Desktop."
