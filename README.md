# event planner


This project is a simple solution for business and its customers for convenient interaction.
It is a platform where a business announces an event and receives information about its visitors, and the user 
can find out detailed information about it and make an appointment to attend it.

## Database

Application use PostgreSQL database. For start the application you need Postgres server (jdbc:postgresql://localhost:
5432/eventPlanner) with created database 'eventsDB'. Database contains four tables.

* Table _users_ - contains information about application users;
* Table _events_ - contains information about created events: date, description and venue;
* Table _place_ - contains information about places where the event is held;
* Table _l_users_events_ - link table that contains information about users who have signed up for events;

## Available endpoints for unauthorized users

* http://localhost:8080/user - POST method, registration
* http://localhost:8080/place/{id} - GET method, info about place
* http://localhost:8080/place - GET method, info about all places
* http://localhost:8080/event/{id} - GET method, info about event
* http://localhost:8080/event/countOfVisitors/{id} - GET method, count of visitors of event
* http://localhost:8080/event - GET method, info about all events

## Available endpoints for users

* http://localhost:8080/user/{id} - GET method, info about user
* http://localhost:8080/user/myEvents/{id} - GET method, list of events that the user has signed up for
* http://localhost:8080/user/addEvent - POST method, registration for event
* http://localhost:8080/user - PUT method, change current user
* http://localhost:8080/user - DELETE method, delete current user's account
* http://localhost:8080/user/deleteEvent - DELETE method, delete current user's registration for event

* http://localhost:8080/place/{id} - GET method, info about place
* http://localhost:8080/place - GET method, info about all places

* http://localhost:8080/event/{id} - GET method, info about event
* http://localhost:8080/event - GET method, info about all events
* http://localhost:8080/event/countOfVisitors/{id} - GET method, count of visitors of event

## Available endpoints for placeAdmins

* http://localhost:8080/user/{id} - GET method, info about user
* http://localhost:8080/user - POST method, registration
* http://localhost:8080/user - PUT method, change current user
* http://localhost:8080/user - DELETE method, delete current user's account

* http://localhost:8080/place/{id} - GET method, info about place
* http://localhost:8080/place - GET method, info about all places
* http://localhost:8080/place - PUT method, change info about place, the administrator of which is this placeAdmin

* http://localhost:8080/event/{id} - GET method, info about event
* http://localhost:8080/event - GET method, info about all events
* http://localhost:8080/event/countOfVisitors/{id} - GET method, count of visitors of event
* http://localhost:8080/event/visitors - GET method, info about all visitors of event(only for events in place,
administered this placeAdmin)
* http://localhost:8080/event - POST method, create event in administrated place
* http://localhost:8080/event - PUT method, edit event in administrated place
* http://localhost:8080/event/{id} - DELETE method, delete event in administrated place

## Available endpoints for admins

* http://localhost:8080/user/{id} - GET method, info about user
* http://localhost:8080/user - GET method, info about all users
* http://localhost:8080/user/myEvents/{id} - GET method, list of events that the user has signed up for
* http://localhost:8080/user/addEvent - POST method, registration for event
* http://localhost:8080/user - PUT method, change current user
* http://localhost:8080/user - DELETE method, delete current user's account
* http://localhost:8080/user/deleteEvent - DELETE method, delete current user's registration for event

* http://localhost:8080/place/{id} - GET method, info about place
* http://localhost:8080/place - GET method, info about all places
* http://localhost:8080/place - POST method, create new place
* http://localhost:8080/place - PUT method, change info about any place
* http://localhost:8080/place/admin - PUT method, appoint admin to place
* http://localhost:8080/place/{id} - DELETE method, delete place

* http://localhost:8080/event/{id} - GET method, info about event
* http://localhost:8080/event - GET method, info about all events
* http://localhost:8080/event/countOfVisitors/{id} - GET method, count of visitors of event
* http://localhost:8080/event/visitors - GET method, info about all visitors of any event
* http://localhost:8080/event - POST method, create event
* http://localhost:8080/event - PUT method, edit event
* http://localhost:8080/event/{id} - DELETE method, delete event
* http://localhost:8080/event/pastEvents - DELETE method, delete all events with past date