@echo off
COLOR fd
cls
echo Inizio Lavorazione Test...
"\\192.168.1.18\PrintNet T Designer 7\PNetTC.exe" "\\192.168.1.18\Lavori IPA\Programmi\MaranIfis\Maran_Ifis.wfd" -e PDF -o "AFP" -f "C:\AFP\MARAN_IFIS\tmp\%%h[3].%%e"
::TIMEOUT /T 10
::exit;
