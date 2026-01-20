 #!/bin/bash

 POSTS=(
   '{"text":"Hello everyone! This is my first post on the
 platform.","userId":1}'
   '{"text":"Just finished an amazing project with my
 team!","userId":2}'
   '{"text":"Beautiful day today, feeling great!","userId":3}'
   '{"text":"Anyone up for coffee later?","userId":4}'
   '{"text":"Working on some cool new features for the
 app.","userId":5}'
   '{"text":"Just launched my new project, check it out!","userId":6}'
   '{"text":"Excited to connect with everyone here!","userId":7}'
   '{"text":"Learning something new every day.","userId":1}'
   '{"text":"Great discussions happening in the
 community.","userId":2}'
   '{"text":"Looking forward to the weekend!","userId":3}'
   '{"text":"Just completed a challenging task
 successfully!","userId":4}'
   '{"text":"Sharing my thoughts on the latest industry
 trends.","userId":5}'
   '{"text":"Had a productive day at work today.","userId":6}'
   '{"text":"Can'\''t wait to meet everyone at the next
 meetup!","userId":7}'
 )

 for post in "${POSTS[@]}"; do
   wget --post-data="$post" \
     --header='Content-Type: application/json' \
     http://localhost:9090/api/v1/posts
   echo ""
 done
