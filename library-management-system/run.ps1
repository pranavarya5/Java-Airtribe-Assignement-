# PowerShell Script to Compile and Execute the Java Library Management System

$ErrorActionPreference = "Stop"

$scriptDir = Split-Path -Parent $MyInvocation.MyCommand.Path
$srcDir = Join-Path $scriptDir "src"
$binDir = Join-Path $scriptDir "bin"

Write-Host "==========================================" -ForegroundColor Cyan
Write-Host " Building Library Management System...    " -ForegroundColor Cyan
Write-Host "==========================================" -ForegroundColor Cyan

if (Test-Path $binDir) {
    Remove-Item -Recurse -Force $binDir
}
New-Item -ItemType Directory -Path $binDir | Out-Null

$javaFiles = Get-ChildItem -Path $srcDir -Recurse -Filter "*.java" | Select-Object -ExpandProperty FullName

Write-Host "Compiling $($javaFiles.Count) Java source files..." -ForegroundColor Yellow
& javac -d $binDir -sourcepath $srcDir $javaFiles

if ($LASTEXITCODE -eq 0) {
    Write-Host "Compilation successful!" -ForegroundColor Green
    Write-Host "`nRunning Main Application..." -ForegroundColor Cyan
    Write-Host "------------------------------------------" -ForegroundColor Gray
    & java -cp $binDir com.library.Main
} else {
    Write-Host "Compilation failed with exit code $LASTEXITCODE" -ForegroundColor Red
}
