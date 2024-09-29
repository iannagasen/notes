

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
  8. when connected using ssh, docker run -p 80:80 nginx
  9. add ec2 security group for 80 outbound rule
  10. create another ec2 instance for root user
  11. if you switch to the other root user, you can still see the newly created ec2(also ssh)
      1.  how to make the new created instances not visible to the new user
      2.  HOW TO DO??
          1.  create an inline policy
              1.  choose a service
                  1.  ec2 instance connect
              2.  create polic
              3.  actions - deny
              4.  paste there ec2 AR
      3.  try to connect again


IAM roles,
ec2 instance wants to talk to s3

  1. create in instance using root acc
  2. connect using ssh
     1. commands
        1. aws
        2. aws s3 ls
           1. - unable to locate credentials
           2. this means the ec2 instance(**NOT THE USER**) does not have credentials to access s3 service
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
  11. now go to the ssh connecte
      1.  aws s3 ls
      2.  aws s3 ls s3://vins-demo/
  12. ASSIGNMENT: copy a file from s3 to ec2
  13. ASSIGNMENT: upload a file
  14. DELETE ROLES and INSTANCES


Access keys for local development
  - we might want to write a file to s3 from local machine
    - first inslall the aws cli
    - test: `aws` from ur cli
    - go back to aws console, root acount
      - aassign the iam account(assuming this account wants to write a file to s3 from local)
      - WHAT TO DO;
        - `Create access keys`
          - check local code then create
        - test`aws configure`
          - it will ask for an access key id
          - it will ask for the secret access key
          - region
          - default output format; none
    - ASSIGNMENT: research for the crecedentials dir and use cat
    - test: `aws s3 ls`
      - this will not word because of permission issue  
        - to fix; attach policy; s3 full access
        - now test the cmd again

UPLOADING FILE TO S3 using java

```java
private String BUCKET = "vins-demo"
try ( var client = S3.cilent.builder().region(REGION.US_EAST_1).build() ) {
  // write
  var putRequest = PutRequest.builder()
        .bucket(BUCKET)
        .key("public/hello.txt") 
        .build()

  client.putObject(putRequest, Path.of("/hello.txt"))

  // r
  var getRequest = GetObjectRequest.builder()
        .bucket(BUCKET)
        .key("public/02_aws.png")
        .build()
  clent.getObject(getRequest, Path.of("aws.png"))

}
```
QUESTIONS? Why these work without credetials??
  - because we already sign in using aws cli, using `aws configure`
  - doing these also prevents accidentally pushing of credentials to github


ACCESS KEYS BEST PRACTICES
  - the access keys have the same permission as the user has
  - what will happen when you lost the credentials??
    - deactivate or delete the access key


ROOT ACCOUNT BEST PRACTICES
  - dont use root account for everything
    - unlimited privileges
    - if someone steals the creds
      - they can take down the whole infrastructure 
  - AWS RECOMMENDATION
    - create an user in a group called ADMINISTRATOR
      - also enable MFA for this group/user


RDS
  - relational database systems
  - highly available
  - administrative task (fully managed by aws)
    - patching
    - scheduled maintenance
    - periodic backups
  - features
    - multi AZ (availability zones)
      - one instance available in one az
      - other instance available in other zone
      - these also synchronize
        - 1 update to another will also update other replicas
    - read replicas
      - 1 instance write db
      - n instance read db
        - these read dbs will get updates from write db asynchronously


QUESTIONS:  
1. what is db cluster (vs. db instance)
2. multi az db instance vs single db instance
3. multi az db cluster
  

CREATING RDS DB INSTANCE (can take up to 10 minutes to spun up)
  - create database
  - choose postgresql
  - database name 
  - master username
  - self managed (credential)
  - db instance class
    - db.t3 micro
  - storage
    - general purpose ssd 
    - allocated 20 GiB
  - autoscaling
    - enable
  - connectivity
    - dont connect to an ec2 compute resource
  - security
    - create new sgroup
    - monitoring
  - enable encryption
