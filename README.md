# Developer Notes

## Watch the REST API from Powershell

`while (1) { http get localhost:8080/api/alarms; sleep 5; cls;}`

## HTTPie

`http post localhost:8080/api/alarms name="web.benhunter.me" target="http://web.benhunter.me" webhook="REPLACE_ME"`
