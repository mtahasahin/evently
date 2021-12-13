import React from 'react';
import { useForm } from 'react-hook-form';
import Button from '../../elements/Button/Button';
import EditApi from '../../../api/edit.api';
import { toast } from 'react-toastify';
import Modal from '../../elements/Modal/Modal';
import { IoIosClose } from 'react-icons/io';
import FormGroup from '../../elements/FormGroup/FormGroup';
import TextInput from '../../elements/TextInput/TextInput';
import useAuth from '../../../hooks/useAuth';
import ErrorMessage from '../../elements/ErrorMessage/ErrorMessage';

const CloseAccountPage = () => {
  const { logout } = useAuth();
  const [modalIsOpen, setIsOpen] = React.useState(false);
  const {
    register,
    control,
    handleSubmit,
    watch,
    formState: { errors },
    reset,
    setError,
    clearErrors,
  } = useForm();
  const onSubmit = (data) => {
    EditApi.closeAccount({ ...data })
      .then((res) => {
        logout();
      })
      .catch((err) => {
        toast(err.response.data.message, { type: 'error' });
        err.response?.data?.errors.forEach((e) => {
          setError(e.field, {
            type: 'value',
            message: e.message,
          });
        });
      })
      .finally(() => {
        reset({}, { keepErrors: true });
      });
  };

  return (
    <div className="shadow-sm">
      <div className="bg-white rounded-t h-full w-full py-10 px-8">
        <div className="flex flex-col">
          <h1 className="text-3xl font-bold mb-8">Close Account</h1>
          Thank you for using Evently. If there is anything we can do to keep
          you with us, please let us know.
        </div>
      </div>
      <div className="w-full px-8 py-6 bg-gray-50 rounded-b flex flex-row justify-end">
        <Button onClick={() => setIsOpen(true)} appearance="dark" size="lg">
          Close Account
        </Button>
      </div>
      <Modal isOpen={modalIsOpen} setOpen={setIsOpen}>
        <form onSubmit={handleSubmit(onSubmit)}>
          <div className="p-4 border-b flex justify-between">
            <div className="text-lg font-bold">Close Account</div>
            <IoIosClose
              size="1.5rem"
              className="cursor-pointer"
              onClick={() => setIsOpen(false)}
            />
          </div>
          <div className="p-4 border-b flex flex-col">
            <div className="pb-5">
              You need to enter your password to close your account.
            </div>
            <FormGroup>
              <div className="w-full flex flex-col gap-y-2">
                <TextInput
                  type="password"
                  register={register}
                  name="password"
                  placeholder="Enter password"
                  error={errors.password}
                />
                <ErrorMessage messages={errors.password?.message} />
              </div>
            </FormGroup>
          </div>
          <div className="p-4  flex flex-row justify-end gap-x-2">
            <Button
              type="button"
              onClick={() => setIsOpen(false)}
              size="lg"
              appearance="white"
            >
              Cancel
            </Button>
            <Button size="lg" appearance="danger">
              Close Account
            </Button>
          </div>
        </form>
      </Modal>
    </div>
  );
};

export default CloseAccountPage;
