(echo sel vol G & echo list vol & echo attr disk set readonly & echo detail disk) | diskpart
echo.
echo.
if %ERRORLEVEL% == 0 (
  echo SUCCESS! Drive G should now be READONLY.
) else (
  echo Failure setting G to READONLY.
)