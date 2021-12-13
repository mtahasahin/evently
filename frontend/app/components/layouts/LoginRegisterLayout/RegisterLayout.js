import React from 'react';
import LoginRegisterLayout from './LoginRegisterLayout';
import Link from 'next/link';

const RegisterLayout = (page) => {
  const footerContent = (
    <div className="text-center text-gray-400">
      Already have an account?{' '}
      <Link href="/login">
        <a className="text-gray-600 hover:underline">Log In</a>
      </Link>
    </div>
  );

  return (
    <LoginRegisterLayout
      backgroundImageUrl="/register.jpg"
      headerText="Let's Start"
      footerContent={footerContent}
    >
      {page}
    </LoginRegisterLayout>
  );
};

export default RegisterLayout;
