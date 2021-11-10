import React from 'react';
import RegisterLayout from "../app/components/layouts/LoginRegisterLayout/RegisterLayout";
import RegisterPage from "../app/components/templates/RegisterPage";


const Signup = () => (
    <RegisterPage/>
)

Signup.getLayout = page => RegisterLayout(page)

export default Signup;