@rem
@rem Copyright 2015 the original author or authors.
@rem
@rem Licensed under the Apache License, Version 2.0 (the "License");
@rem you may not use this file except in compliance with the License.
@rem You may obtain a copy of the License at
@rem
@rem      https://www.apache.org/licenses/LICENSE-2.0
@rem
@rem Unless required by applicable law or agreed to in writing, software
@rem distributed under the License is distributed on an "AS IS" BASIS,
@rem WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
@rem See the License for the specific language governing permissions and
@rem limitations under the License.
@rem

@if "%DEBUG%" == "" @echo off
@rem ##########################################################################
@rem
@rem   Gradle start up script for Windows
@rem
@rem ##########################################################################

@rem Set local scope for the variables with windows NT shell
if "%OS%"=="Windows_NT" setlocal

@rem Attempt to set APP_HOME

@rem Resolve links: %0 may be a link
set APP_HOME=%~dp0

:loop
if exist "%APP_HOME%\gradle-wrapper.jar" goto findJavaFromWrapper
set APP_HOME=%APP_HOME%\..
if exist "%APP_HOME%\gradle-wrapper.jar" goto findJavaFromWrapper
set APP_HOME=%APP_HOME%\..
if exist "%APP_HOME%\gradle-wrapper.jar" goto findJavaFromWrapper
set APP_HOME=%APP_HOME%\..
if exist "%APP_HOME%\gradle-wrapper.jar" goto findJavaFromWrapper
goto fail

:findJavaFromWrapper
set JAVA_EXE=%JAVA_HOME%

if not exist "%JAVA_EXE%\bin\java.exe" (
    set JAVA_EXE=java
)

:execute
@rem Setup the command line

set CLASSPATH=%APP_HOME%\gradle\wrapper\gradle-wrapper.jar


@rem Execute Gradle
"%JAVA_EXE%" %DEFAULT_JVM_OPTS% %JAVA_OPTS% %GRADLE_OPTS% -classpath "%CLASSPATH%" org.gradle.wrapper.GradleWrapperMain %*

:end
@rem End local scope for the variables with windows NT shell
if "%OS%"=="Windows_NT" endlocal

:omega
exit /b 1

:fail
echo.
echo ERROR: Unable to find gradle-wrapper.jar, please ensure the environment variable GRADLE_USER_HOME points to a correctly installed Gradle distribution.
echo.
exit /b 1
