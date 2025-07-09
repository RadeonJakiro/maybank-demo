Welcome to the Demo, 

This is Done with 2 Days (In Trial Mode) which is not familiar with the Framework, but try the best to make it possible based on the knowledge i gained from the experience. Some will explain in the Microsoft Document.

So In order to test the program, 

After obtained the source, you may run gradle wrapper. So you don't have to install gradle.
Start Server
./gradlew bootRun

However you need to install Java JDK.

within this server startup, it will create h2 table file from same directory with /data folder and running few SQL with liquibase db version

---------------------------------------
Core Function to be Test
---------------------------------------
1. First you have to register your own account
URL : http://localhost:8085/api/auth/register (POST Method)
Request Body
{
    "fullName": "Jakiro",
    "userId": "222",
    "email": "jakiro@tester.com",
    "mobileNo": "0169876543210",
    "password": "pass1234"
}


2. Next login with the info you have been registered with
URL : http://localhost:8085/api/auth/login (POST Method)
Request Body
{
    "userId":"222",
    "password":"pass1234"
}

3. After Login, can use API to retrieve payment history
URL : http://localhost:8085/api/payment/history?page=0&size=10 (GET Method)
page = 0, size = 10 (Request Param)
Header
Authorization: Bearer {Token you retrieve from Login}
example: Bearer eyJhbGciOiJIUzI1NiJ9.eyJyb2xlcyI6WyJVU0VSIl0sImZ1bGxOYW1lIjoiSmFraXJvIiwibW9iaWxlTm8iOiIwMTY5ODc2NTQzMjEwIiwiZW1haWwiOiJqYWtpcm9AdGVzdGVyLmNvbSIsInVzZXJuYW1lIjoiMjIyIiwiaWF0IjoxNzUxMzA2NDc2LCJleHAiOjE3NTEzMTAwNzZ9.7b7pjyCXeTBF1X96nVEeDRyNjPCJHnWjeRUBcHrntOQ


4. Batch Job for receive the DataSource and Pump Data
It will run every 10 minutes based on current design.

