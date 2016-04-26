@echo off
setLocal EnableDelayedExpansion
set CLASSPATH="
for /R ./lib %%a in (*.jar) do (
  set CLASSPATH=!CLASSPATH!;%%a
)
set CLASSPATH=!CLASSPATH!"

%JAVA_HOME%\bin\java -client -cp %CLASSPATH% eu.linksmart.services.mr.client.RepoClientApplication %*