@ 2>/dev/null # 2>nul & echo off & goto BOF

:BOF
@echo off
%JAVA_HOME%\bin\java %JAVA_OPTS% -jar "%~dpnx0" %*
exit /B %errorlevel%
