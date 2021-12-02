import React, {useState} from 'react';
import TextInput from "../elements/TextInput/TextInput";
import Button from "../elements/Button/Button";
import {useForm} from "react-hook-form";
import useAuth from "../../hooks/useAuth";
import ErrorMessage from "../elements/ErrorMessage/ErrorMessage";
import Loader from "react-loader-spinner";

const RegisterPage = () => {
    const [errorMessage, setErrorMessage] = useState(null);
    const {signup} = useAuth();
    const [loading, setLoading] = useState(false);
    const onSubmit = data => {
        setLoading(true);
        signup(data.name, data.email, data.password).catch(e => {
            e.response.data.errors?.forEach(e => {
                setError(e.field, e.message)
            })
            setErrorMessage(e.response.data.message)
        }).finally(() => setLoading(false))
    }
    const {register, handleSubmit, setError} = useForm();

    return (
        <form onSubmit={handleSubmit(onSubmit)}>
            <div className="mb-2">
                <TextInput
                    register={register}
                    name={"name"}
                    placeholder="Your Name"
                    required/>
            </div>
            <div className="mb-2">
                <TextInput
                    register={register}
                    name={"email"}
                    placeholder="Email Address"
                    type="email"
                    required/>
            </div>
            <div className="mb-2">
                <TextInput
                    register={register}
                    name={"password"}
                    placeholder="Password"
                    type="password"
                    required/>
            </div>
            <ErrorMessage messages={errorMessage}/>
            <Button type="submit" appearance="secondary" size="xl" fullWidth>
                <span className="font-semibold flex justify-center items-center gap-x-2">Register <Loader type="Oval"
                                                                                                          color="White"
                                                                                                          height={20}
                                                                                                          width={20}
                                                                                                          visible={loading}/></span>
            </Button>
        </form>
    );
};

export default RegisterPage;