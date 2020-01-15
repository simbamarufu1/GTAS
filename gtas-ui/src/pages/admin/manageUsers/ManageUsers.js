import React from "react";
import Table from "../../../components/table/Table";
import { userService } from "../../../services/serviceWrapper";
import Title from "../../../components/title/Title";
import Xl8 from "../../../components/xl8/Xl8";
import { Container, Button } from "react-bootstrap";

const ManageUsers = ({ name }) => {
  const cb = function(result) {};
  console.log(userService.get());
  return (
    <Xl8>
      <Container fluid>
        <Title title={name}></Title>
        <Table
          service={userService.get}
          id="users"
          callback={cb}
          ignoredFields={["roles", "password"]}
        ></Table>
      </Container>
    </Xl8>
  );
};

export default ManageUsers;
