import React from 'react';
import { FcGoogle } from 'react-icons/fc';
import { FaFacebook } from 'react-icons/fa';
import Link from 'next/link';
import Image from 'next/image';
import { FACEBOOK_AUTH_URL, GOOGLE_AUTH_URL } from '../../../constants/oauth';
import { useRouter } from 'next/router';

const LoginRegisterLayout = ({
  backgroundImageUrl,
  headerText,
  footerContent,
  children,
}) => {
  const {
    query: { error },
  } = useRouter();
  return (
    <div className="flex flex-col sm:flex-row h-screen min-h-full">
      <div
        className="sm:w-2/6 sm:h-full h-0 min-h-[250px] bg-center bg-cover bg-no-repeat"
        style={{
          backgroundImage: `url('${backgroundImageUrl}')`,
        }}
      ></div>
      <div className="sm:w-4/6 h-screen min-h-full flex flex-col">
        <div className="flex flex-col justify-start items-center gap-y-2 flex-1 mb-16">
          <div className="my-6 sm:my-12">
            <Link href="/" className="sm:justify-center">
              <a>
                <Image src="/logo.svg" width="170px" height="50px" alt="logo" />
              </a>
            </Link>
          </div>
          <div className="text-3xl pb-2">{headerText}</div>
          <div className="pb-2">Join and Explore Evently</div>
          <div className="w-5/6 sm:w-8/12 md:w-6/12 lg:w-5/12 xl:x-4/12">
            <div className="flex flex-col gap-y-2">
              <Link href={GOOGLE_AUTH_URL}>
                <a className="bg-blue-500 text-white text-center font-semibold py-3 rounded relative cursor-pointer select-none">
                  <div className="bg-white absolute left-1.5 top-1.5 bottom-1.5 rounded">
                    <FcGoogle size="2.2em" />
                  </div>
                  Connect with Google
                </a>
              </Link>
              <Link href={FACEBOOK_AUTH_URL}>
                <a className="bg-blue-700 text-white text-center font-semibold py-3 rounded relative cursor-pointer select-none">
                  <div className="absolute left-1.5 top-1.5 bottom-1.5 rounded">
                    <FaFacebook size="2.2em" />
                  </div>
                  Connect with Facebook
                </a>
              </Link>
            </div>
            {error && (
              <div className="text-red-500 text-center mt-2">
                Error: {error}
              </div>
            )}
            <div className="text-sm text-gray-400 py-4 text-center">OR</div>
            {children}
          </div>
        </div>
        <div className="w-full bg-gray-200 flex justify-center py-[15px] border-b border-t border-gray-300">
          {footerContent}
        </div>
      </div>
    </div>
  );
};

export default LoginRegisterLayout;
