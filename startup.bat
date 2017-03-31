@echo off

set curdir=%~dp0
set java=C:\ecc\java\jre8\bin\javaw.exe
cd /d %curdir%
cd ../
set APP_HOME=%cd%

if not exist C:\ecc\Terminal\logs (
  md C:\ecc\Terminal\logs
)

set APP_LOG=C:\ecc\Terminal\logs
set APP_MAIN=com.easyget.terminal.bootstrap.BootStrap
set OP_LOG_FILE=C:\ecc\Terminal\logs\SHELL_OP.log
set JAVA_OPTS=-Duser.timezone=GMT+8 -Dfile.encoding=UTF-8 -server -Xms512m -Xmx512m -Xloggc:%APP_LOG%\gc.log
set CLASSPATH=C:\ecc\Terminal\conf
	
SETLOCAL ENABLEDELAYEDEXPANSION 
for /R %%s in (lib\*.jar) do ( 
set CLASSPATH=!CLASSPATH!;%%s
)
set startCmd=%java% %JAVA_OPTS% %APP_MAIN% -classpath %CLASSPATH% > %APP_LOG%\nohup.log"
start  %startCmd%

exit