@ECHO OFF
setlocal
for /f "tokens=3" %%g in ('java -version 2^>^&1 ^| findstr /i "version"') do (
    set JAVAVER=%%g
)
set JAVAVER=%JAVAVER:"=%
for /f "delims=. tokens=1-3" %%v in ("%JAVAVER%") do (
	set VER=%%w
)
IF %VER%==8 (
   java -jar BlackJack.jar
) ELSE (
   echo You need Java 8 to run this game, and you hava java %JAVAVER% installed. Your java is version %VER%
)
endlocal