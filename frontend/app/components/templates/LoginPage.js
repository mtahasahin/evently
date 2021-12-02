import React, {useState} from 'react';
import TextInput from "../elements/TextInput/TextInput";
import Button from "../elements/Button/Button";
import useAuth from "../../hooks/useAuth";
import {useForm} from "react-hook-form";
import ErrorMessage from "../elements/ErrorMessage/ErrorMessage";
import Loader from "react-loader-spinner";

const LoginPage = () => {
    const [errorMessage, setErrorMessage] = useState(null);
    const {login} = useAuth();
    const [loading, setLoading] = useState(false);
    const onSubmit = data => {
        setLoading(true);
        login(data.email, data.password).catch(e => {
            clearErrors();
            setError("email", {type: "value"})
            setError("password", {type: "value"})
            setErrorMessage(e.response.data.message)
        }).finally(() => setLoading(false));
    }
    const {register, handleSubmit, formState: {errors}, setError, clearErrors} = useForm();

    return (
        <form onSubmit={handleSubmit(onSubmit)}>
            <div className="mb-2">
                <TextInput
                    register={register}
                    name={"email"}
                    placeholder="Email address"
                    error={errors.email}
                    required/>
            </div>
            <div className="mb-2">
                <TextInput
                    register={register}
                    name={"password"}
                    placeholder="Password"
                    type="password"
                    error={errors.password}
                    required/>
            </div>
            <ErrorMessage messages={errorMessage}/>
            <Button type="submit"
                    appearance="secondary" size="xl" fullWidth>
                <span className="font-semibold flex justify-center items-center gap-x-2">
                    Log in
                    <Loader type="Oval"
                            color="White"
                            height={20}
                            width={20}
                            visible={loading}/>
                </span>
            </Button>
        </form>
    );
};

export default LoginPage;