build:
	javac src/cs155/jray/* -d bin

%:
	java -cp bin cs155.jray.DemoScene$@
