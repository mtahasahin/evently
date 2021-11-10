import React from 'react';
import {useForm} from "react-hook-form";
import FormGroup from "../../elements/FormGroup/FormGroup";
import TextInput from "../../elements/TextInput/TextInput";
import Button from "../../elements/Button/Button";
import EditApi from "../../../api/edit.api";
import timezones from "../../../constants/timezone-list"
import languages from "../../../constants/language-list";
import {toast} from 'react-toastify';
import ErrorMessage from "../../elements/ErrorMessage/ErrorMessage";

const timezoneOptions = timezones.map(e => ({name: e, value: e}))
const languageOptions = languages.map(e => ({name: `${e.name} (${e.nativeName})`, value: e.code}))
const profileVisibilityOptions = [{name: "Public", value: "true"}, {name: "Private", value: "false"}];


const ChangePasswordPage = () => {

    const {register, control, handleSubmit, watch, formState: {errors}, reset, setError, clearErrors} = useForm();
    const onSubmit = data => {
        if (data.newPassword !== data.newPasswordConfirm) {
            setError("newPassword", {type: "value", message: "The password confirmation does not match."});
            clearErrors();
            return;
        }
        EditApi.changePassword({...data}).then(res => {
            toast(res.data.message, {type: "success"})
        }).catch(err => {
            toast(err.response.data.message, {type: "error"});
            err.response.data.errors.forEach(e => {
                setError(e.field, {type: "value", message: e.message});
            })
        }).finally(() => {
            reset({}, {keepErrors: true});
        })
    }
    return (
        <form onSubmit={handleSubmit(onSubmit)}>
            <div className="shadow-sm">
                <div className="bg-white rounded-t h-full w-full py-10 px-8">
                    <div className="flex flex-col">
                        <h1 className="text-3xl font-bold mb-8">Change Password</h1>
                        <FormGroup>
                            <div className="w-full">
                                <TextInput type="password" label="CURRENT PASSWORD" name="currentPassword"
                                           register={register} error={errors.currentPassword}/>
                                <ErrorMessage messages={errors.currentPassword?.message}/>
                            </div>
                            <div className="w-full"></div>
                        </FormGroup>
                        <FormGroup>
                            <div className="w-full">
                                <TextInput type="password" label="NEW PASSWORD" name="newPassword" register={register}
                                           error={errors.newPassword}/>
                                <ErrorMessage messages={errors.newPassword?.message}/>
                            </div>
                            <div className="w-full">
                                <TextInput type="password" label="RE-TYPE NEW PASSWORD" name="newPasswordConfirm"
                                           register={register}/>
                            </div>
                        </FormGroup>
                    </div>
                </div>
                <div className="w-full px-8 py-6 bg-gray-50 rounded-b flex flex-row justify-end">
                    <Button type="submit" appearance="dark" size="lg">Save Changes</Button>
                </div>
            </div>
        </form>
    );
};

export default ChangePasswordPage;