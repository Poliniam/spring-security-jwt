The aim of the project is to give an independent rating to traders
game items. The rating is based on
reviews that everyone offers, while reviews are thorough
verification by trusted persons. The overall top is based on these ratings.
traders in a particular category of games.

## How to use this app:
If you want to use the application as an administrator, enter the "password"(imadmin28284646) and "login"(Administrator) following the link: /auth
## Administrator functions
GET /admin/objects - get all game objects;  
GET /admin/objects/{id} - approve a new game object;  
GET /admin/comments - get all comments;  
PUT /admin/comments/{id} - approve a new comment;  
POST /admin/game - add a new game;  
PUT admin/games/{id} - update a game;  
DELETE /admin/users/{id}/comments/{idComm} - to delete a comment;  


## Trader functions
First, you must register using the link:POST /register. Then, confirm the activation code. Then, authenticate:POST /auth.  
If you forgot your password, restore it using the link POST /forgetPassword. Then activation code again. Then  
make a new password POST /newPassword.  
POST /user/object - to make new game object;  
GET /object/{id} - get one game object;  
DELETE /object/{id} - to delete (only your) game object;  
PUT /user/object/{id} - to edit (only your) gan=me object;  
GET /user/my - to get your game objects;  

## Anonim functions
You don't need to register to be an anonim  
GET /rating/{id} - to see the rating or the trader;  
POST /articles/{id}/comments - to create a comment;  
GET /users/{id}/comments - to get comments of a trader;  
GET /users/{id}/comments/{idComm} - to get a comment of a trader; 
GET /games - to get all games;  
GET /object - to see all game objects;  
GET /object/{id} - to get a game object;  
GET /filterByGames/{gameName} - to get game objects of definite game

