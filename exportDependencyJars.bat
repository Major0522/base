@echo off
echo --------------------------------------
echo Project:cabinet_terminal
echo FAQ:niujunhong
echo --------------------------------------
set curdir=%~dp0
cd /d %curdir%

set pName=plugin_base
if exist OUT_dependencyJars (
   del /f/s/q OUT_dependencyJars\*.*
) else (
    md OUT_dependencyJars
)

if exist C:\temp\%pName%\lib\ (
   del /f/s/q C:\temp\%pName%\lib\*.*
)


call mvn clean
call  mvn -Dmaven.test.skip=true install
call mvn dependency:copy-dependencies -DoutputDirectory=lib -DincludeScope=runtime
copy  C:\temp\%pName%\lib\*.jar  OUT_dependencyJars\
echo export done!
pause