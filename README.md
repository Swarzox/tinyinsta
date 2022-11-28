
<h1 align="center">
  <br>
  <a href="https://cdn-icons-png.flaticon.com/512/87/87390.png" alt="" width="200"></a>
  <br>
  TinyInsta
  <br>
</h1>

<h4 align="center">TinyInsta, a scalable Instagram Like.</h4>

<p align="center">
  <a href="#features>Features</a> •
  <a href="#screenshot">Screenshot</a> •
  <a href="#datastore">Datastore</a> •
  <a href="#api explorer">API explorer</a> •
  <a href="#url">URL</a>
  <a href="#how to install">How to install</a>
</p>

## Features

* Disconnection and connection system using google GSI
* Keep session alive
* Fetch users profile
* Follow/Unfollow user from user profile
* Like/unlike post from user profile
* Upload a picture and store it directly to the datastore
* Retrieving most recent posts from your subscriptions (feed)
* Like/unlike post from feed

## Screenshot

* Fetch a user profile
![Interface](https://img001.prntscr.com/file/img001/krxOPpv8QxenO1P_uaoexw.png "alexis' profile")

* Your own profile
![Own profile](https://img001.prntscr.com/file/img001/5SablTgISN2lIsF5UuPdFA.png "Your profile")

* Fetch most recents posts from your subscriptions (feed)
![Feed](https://img001.prntscr.com/file/img001/wxEhY2daRDSp9TId5NwnbQ.png "Feed")

## Datastore

* User table
![user table](https://img001.prntscr.com/file/img001/ZJ2VA51jTp26lb0KrpX0fQ.png "User table")

* Post table
![post table](https://img001.prntscr.com/file/img001/eZV47pu1QO2jqtoTckim6g.png "Post table")

## API Explorer

[API Explorer](https://web-cloud-datastore-363112.ew.r.appspot.com/_ah/api/explorer)

## URL

[TinyInsta](https://web-cloud-datastore-363112.ew.r.appspot.com/)

## How To Install

To clone and run this application, you'll need [Git](https://git-scm.com) from your command line:

```bash
# Create a new folder
$ mkdir TinyInsta

# Go into the new folder
$ cd TinyInsta

# Clone this repository
$ git clone https://github.com/Swarzox/tinyinsta.git

# Install
$ mvn install

# Deploy locally
$ mvn appengine:run

# Deploy on google's servers
$ mvn appengine:deploy

#Retrieve url
$ gcloud app browse
```
