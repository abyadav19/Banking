Note :
Secretkey and Token time out is configurable in application.properties. As of now server will starts on default port.
only "/abcbank/openAccount" and "/abcbank/authenticate" APIs are allowed without JWT token.
API /abcbank/openAccount: response contains the userName, Password. Response also contains Jwt token to communicate with other api.
1. Request method : POST
http://localhost:8080/abcbank/openAccount
{
	"firstName": <"customer name">,
	"lastName" :<"customer lastName">,
	"idDetails" :<"customer ID">,
    "openingAmount" : <amount>
}
Response example :
{
    "accountHolder": {
        "idDetails": "AbhiPan2",
        "firstName": "Abhi",
        "lastName": "yadav",
        "openingAmount": 10.0
    },
    "accountNumber": 856030025,
    "pin": 3155,
    "balance": 10.0,
    "jwtResponse": {
        "jwt": "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiI4NTYwMzAwMjUiLCJleHAiOjE2NDI5NDIyMjEsImlhdCI6MTY0MjkxMjIyMX0.UNCtXtis3yG6FyK-IOQmBxQ-9aiKguAcveGob20R9hc"
    },
    "userName": "856030025",
    "password": "e08TlG35",
    "idNumber": "AbhiPan2"
}
2. Request method : POST
http://localhost:8080/abcbank/authenticate
{
    "userName" : <"userName">,
    "password" : <"password">
}
Response example:
{
    "jwt": "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxODQxMjU4MzciLCJleHAiOjE2NDI5MDc3NTIsImlhdCI6MTY0MjkwNzcyMn0.KcI4xVGDFBggIm2xAC79Aq5Y2J4XwGuWKH7-_pmBv2k"
}
3. Request method : POST
Request header : Authorization = "Bearer <JWT-Token>"
http://localhost:8080/abcbank/balanceEnquiry
{
    "accountNumber" : <accountNumber>,
    "pin" : <pin>
}

4. Request method : POST
Request header : Authorization = Bearer <JWT-Token>
http://localhost:8080/abcbank/depositAmount
{
    "accountNumber" : <accountNumber>,
    "pin" : <pin>
	"amount" : 0
}

5. Request method : POST
Request header : Authorization = Bearer <JWT-Token>
http://localhost:8080/abcbank/debitAmount
{
    "accountNumber" : <accountNumber>,
    "pin" : <pin>
	"amount" : 0
}

6. Request method : POST
Request header : Authorization = Bearer <JWT-Token>
http://localhost:8080/abcbank/transferFund
{
    "accountNumber" : <accountNumber>,
    "pin" : <pin>
	"amount" : 0,
	"targetAccountNumber" : <targetAccountNumber>,
}

7. Request method : POST
Request header : Authorization = Bearer <JWT-Token>
http://localhost:8080/abcbank/getBankStatement
{
    "accountNumber" : <accountNumber>,
    "pin" : <pin>
}