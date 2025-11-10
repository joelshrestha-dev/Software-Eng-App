Hi, so everyone needs to have their own client secret file here. Do not share yours because security. 

To make one, click: https://console.cloud.google.com/welcome?project=mystical-method-477804-g9 and sign on to google
and make a project. After you make a project, go to library and enable google calendar api. 

Then Go to APIs & Services → Credentials. Click Create Credentials → OAuth client ID.
If prompted, configure the OAuth consent screen:
Choose External for testing.
Fill in App name, User support email, and Developer email address.
Click Save and Continue (other steps can be skipped for testing).
After the consent screen is done, go back to Create OAuth client ID.
Select Desktop app as the application type.
Give it a name like CalendarApp and click Create

Go back to apis and services -> credentials and click on your desktop client edit and u should see
a client secret file that you can download at the bottum. 

Save that in this folder. Rename it client_secret. 
