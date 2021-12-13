import React from 'react';
import LoginLayout from '../app/components/layouts/LoginRegisterLayout/LoginLayout';
import LoginPage from '../app/components/templates/LoginPage';

const Login = function () {
  return <LoginPage />;
};

Login.getLayout = (page) => LoginLayout(page);

export default Login;
