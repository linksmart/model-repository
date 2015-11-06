echo starting iml-sosi-client...
@echo off
setLocal EnableDelayedExpansion
set CLASSPATH="
for /R ./lib %%a in (*.jar) do (
  set CLASSPATH=!CLASSPATH!;%%a
)
set CLASSPATH=!CLASSPATH!"
echo !CLASSPATH!
java -client -cp %CLASSPATH% de.fraunhofer.e3.dmc.client.DMCApplication "$@";