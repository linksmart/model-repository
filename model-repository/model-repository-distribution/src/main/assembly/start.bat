echo starting model-repository...
@echo off
setLocal EnableDelayedExpansion
set CLASSPATH="
for /R ./lib %%a in (*.jar) do (
  set CLASSPATH=!CLASSPATH!;%%a
)
set CLASSPATH=!CLASSPATH!"

java -client -cp %CLASSPATH% eu.linksmart.services.mr.MRApplication "$@";