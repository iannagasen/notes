

IAM
  - Identity Access Management

FAQS
  - User groups vs. roles

4 Components
  - Policy
  - User Groups
  - Users 
  - Roles


Least Privilege Principle

Creating a user 
  1. go to IAM
  2. go to users
  3. create user(no role, no group)
  4. create username
  5. check - provide access to management console
  6. create a custom password
  7. uncheck - password creation at sign in
  8. create user
  9. copy the sign in url
  10. then sign in
  11. NOTE: you may notice that most of the services are disabled
  12. NOTE: you can add a permission in the user, but its NOT GOOD PRACTICE
  

CREATING A GROUPz
  1. go to user group
  2. create user group
  3. attach permission
     1. ex. add `ec2FullAccess```
  4. create user group
  5. add users to the group
     1. add the created user in previous section
     2. test if you can now access ec2
  6. create an ec2
  7. try to connect to ssh, using the created user
     1. NOTE: this will not connect,
     2. you need to add permission `ec2InstanceConnect`
  8. when connected using ssh, docker run -p 80:80 hginc
  9. add ec2 security group for 80 outbound rule
  10. create another ec2 instance for root user
  11. if you switch to the other root user, you can still see the newly created ec2(also ssh)
      1.  how to make the new created instances not visible to the new user
      2.  HOW TO DO??
          1.  create an inline policy
              1.  choose a service
                  1.  ec2 instance connect
              2.  actions - deny
              3.  paste there ec2 ARN
              4.  create policy
      3.  try to connect again


IAM roles, 
ec2 instance wants to talk to s3

  1. create in instance using root acc
  2. connect using ssh
     1. commands
        1. aws
        2. aws s3 ls
           1. - unable to locate credentials
           2. this means the ec2 instance(**NOT THE USER**) does not have credentials
  3. IAM > ROLE >  create a role
  4. select entity type - service
  5. select ec2
  6. select amazon s3 access only
  7. go to instance actions > security > IAM roles
     1. note: you cant see the iam role you created
     2. why? because you the user dont have the permission to see the iam roles
  8. go to user groups, go to the created group > add permission
     1. iam > list instance policies > allow
     2. create name > ec2-modify
  9. goi to instance actions > security > IAM ROLES
     1.  now you can see the roles, but cant assign
     2.  go to ec2-modify permission and edit actions JSON
         1.  add `iam:PassRole`
  10. now attach the role
  11. now go to the ssh connected
      1.  aws s3 ls
      2.  aws s3 ls s3://vins-demo/
  12. ASSIGNMENT: copy a file from s3 to ec2
  13. ASSIGNMENT: upload a file
  14. DELETE ROLES and INSTANCES