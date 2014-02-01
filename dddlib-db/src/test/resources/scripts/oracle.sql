drop table TEST_AUTH;

-- Create table
create table TEST_AUTH
(
  ID   NUMBER not null,
  NAME VARCHAR2(10)
)
tablespace TBS_RAMS_DATA
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    next 1M
    minextents 1
    maxextents unlimited
  );
-- Create/Recreate primary, unique and foreign key constraints 
alter table TEST_AUTH
  add constraint PK_TEST_ID primary key (ID)
  using index 
  tablespace TBS_RAMS_DATA
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    next 1M
    minextents 1
    maxextents unlimited
  );
exit;