import React, { useEffect } from 'react';
import { useForm } from 'react-hook-form';
import FormGroup from '../../elements/FormGroup/FormGroup';
import TextInput from '../../elements/TextInput/TextInput';
import DatePicker from '../../elements/DatePicker/DatePicker';
import Select from '../../elements/Select/Select';
import { fuzzySearch } from 'react-select-search-nextjs';
import TextArea from '../../elements/TextArea/TextArea';
import Button from '../../elements/Button/Button';
import SocialMediaInput from '../../elements/SocialMediaInput/SocialMediaInput';
import EditApi from '../../../api/edit.api';
import timezones from '../../../constants/timezone-list';
import languages from '../../../constants/language-list';
import { toast } from 'react-toastify';
import ErrorMessage from '../../elements/ErrorMessage/ErrorMessage';
import useAuth from '../../../hooks/useAuth';

const timezoneOptions = timezones.map((e) => ({ name: e, value: e }));
const languageOptions = languages.map((e) => ({
  name: `${e.name} (${e.nativeName})`,
  value: e.code,
}));
const profileVisibilityOptions = [
  { name: 'Public', value: 'true' },
  { name: 'Private', value: 'false' },
];

const EditProfilePage = () => {
  const { reload } = useAuth();

  useEffect(() => {
    EditApi.getProfile().then((res) => {
      const values = res.data.data;
      values.profile.profilePublic = String(values.profile.profilePublic);
      if (values.profile.dateOfBirth == null) {
        values.profile.dateOfBirth = new Date(1990, 1, 1);
      }
      reset(values);
    });
  }, [reset]);
  const {
    register,
    control,
    handleSubmit,
    formState: { errors },
    reset,
    setError,
  } = useForm();
  const onSubmit = (data) => {
    EditApi.updateProfile({ ...data })
      .then((res) => {
        toast(res.data.message, {
          type: 'success',
        });
        reload();
      })
      .catch((err) => {
        toast(err.response.data.message, { type: 'error' });
        err.response?.data?.errors?.forEach((err) => {
          setError(err.field, {
            type: 'value',
            message: err.message,
          });
        });
      });
  };
  return (
    <form onSubmit={handleSubmit(onSubmit)}>
      <div className="shadow-sm mb-6">
        <div className="bg-white rounded-t h-full w-full py-10 px-8">
          <div className="flex flex-col">
            <h1 className="text-3xl font-bold mb-8">Basic Information</h1>
            <FormGroup>
              <div className="w-full">
                <TextInput
                  label="EMAIL"
                  name="email"
                  register={register}
                  error={errors.email}
                />
                <ErrorMessage messages={errors.email?.message} />
              </div>
            </FormGroup>
            <FormGroup>
              <div className="w-full">
                <TextInput
                  label="YOUR NAME"
                  name="profile.name"
                  register={register}
                  error={errors.profile?.name}
                />
                <ErrorMessage messages={errors.profile?.name?.message} />
              </div>
              <div className="w-full">
                <TextInput
                  label="USERNAME"
                  name="username"
                  register={register}
                  error={errors.username}
                />
                <ErrorMessage messages={errors.username?.message} />
              </div>
            </FormGroup>
            <FormGroup>
              <DatePicker
                label="DATE OF BIRTH"
                name="profile.dateOfBirth"
                control={control}
              />
              <TextInput
                label="WHERE DO YOU LIVE"
                name="profile.location"
                register={register}
              />
            </FormGroup>
            <FormGroup>
              <Select
                options={profileVisibilityOptions}
                name="profile.profilePublic"
                control={control}
                label="PROFILE VISIBILITY"
              />
              <Select
                options={timezoneOptions}
                search
                filterOptions={fuzzySearch}
                placeholder="Select your timezone"
                name="profile.timezone"
                control={control}
                label="TIMEZONE"
              ></Select>
              <Select
                options={languageOptions}
                search
                filterOptions={fuzzySearch}
                placeholder="Select language"
                name="profile.language"
                control={control}
                label="LANGUAGE"
              ></Select>
            </FormGroup>
            <FormGroup>
              <TextArea
                label="ABOUT"
                name="profile.about"
                register={register}
              />
            </FormGroup>
          </div>
        </div>
        <div className="w-full px-8 py-6 bg-gray-50 rounded-b flex flex-row justify-end">
          <Button type="submit" appearance="dark" size="lg">
            Save Changes
          </Button>
        </div>
      </div>
      <div className="shadow-sm">
        <div className="bg-white rounded-t h-full w-full py-10 px-8">
          <div className="flex flex-col">
            <h1 className="text-3xl font-bold mb-8">
              Links and Social Networks
            </h1>
            <FormGroup>
              <TextInput
                label="WEBSITE URL"
                name="profile.websiteUrl"
                register={register}
              />
              <SocialMediaInput
                precedingUrl="facebook.com/"
                label="FACEBOOK USERNAME"
                name="profile.facebookUsername"
                register={register}
              />
            </FormGroup>
            <FormGroup>
              <SocialMediaInput
                precedingUrl="twitter.com/"
                label="TWITTER USERNAME"
                name="profile.twitterUsername"
                register={register}
              />
              <SocialMediaInput
                precedingUrl="instagram.com/"
                label="INSTAGRAM USERNAME"
                name="profile.instagramUsername"
                register={register}
              />
            </FormGroup>
            <FormGroup>
              <SocialMediaInput
                precedingUrl="github.com/"
                label="GITHUB USERNAME"
                name="profile.githubUsername"
                register={register}
              />
              <div className="w-full" />
            </FormGroup>
          </div>
        </div>
        <div className="w-full px-8 py-6 bg-gray-50 rounded-b flex flex-row justify-end">
          <Button type="submit" appearance="dark" size="lg">
            Save Changes
          </Button>
        </div>
      </div>
    </form>
  );
};
export default EditProfilePage;
