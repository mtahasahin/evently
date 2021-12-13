import React from 'react';
import LoginRegisterLayout from './LoginRegisterLayout';
import Link from 'next/link';

const LoginLayout = (page) => {
  const footerContent = (
    <div className="text-center text-gray-400">
      Don't have an account?{' '}
      <Link href="/signup">
        <a className="text-gray-600 hover:underline">Sign Up</a>
      </Link>
    </div>
  );

  return (
    <LoginRegisterLayout
      backgroundImageUrl="/login.jpg"
      headerText="Welcome Back"
      footerContent={footerContent}
    >
      {page}
    </LoginRegisterLayout>
  );
};

export default LoginLayout;
