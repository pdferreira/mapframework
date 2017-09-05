@echo on
pushd .
cd /D %~dp0

if not exist bin (
	mkdir bin
) else (
	del /Q bin
)

echo Compiling
scalac -d bin src/examples/*.scala src/view/*.scala src/misc/*.scala && (
	echo Running
	start "" scala -cp bin misc.Test
	popd
)