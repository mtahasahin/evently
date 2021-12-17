import React from 'react';
import { HiOutlineLink } from 'react-icons/hi';
import { AiOutlineGithub } from 'react-icons/ai';
import { FaFacebookSquare, FaInstagram } from 'react-icons/fa';
import { BsTwitter } from 'react-icons/bs';
import useActiveProfile from '../../hooks/useActiveProfile';

const SocialLink = ({ children }) => {
  return (
    <li className="border-b border-gray-200 py-3 text-gray-500 hover:text-blue-600 cursor-pointer flex gap-x-2 items-center">
      {children}
    </li>
  );
};

const ProfileSocial = () => {
  const { profile } = useActiveProfile();
  const exists =
    profile.websiteUrl ||
    profile.twitterUsername ||
    profile.githubUsername ||
    profile.instagramUsername ||
    profile.facebookUsername;
  return (
    exists && (
      <div className="flex flex-col gap-y-1.5">
        <h6 className="text-xs text-gray-700">ELSEWHERE</h6>
        <ul className="flex flex-col text-sm text-gray-400">
          {profile.websiteUrl && (
            <a href={profile.websiteUrl}>
              <SocialLink url={profile.websiteUrl}>
                <HiOutlineLink size="1.2rem" />
                Website
              </SocialLink>
            </a>
          )}
          {profile.twitterUsername && (
            <a href={`https://twitter.com/${profile.twitterUsername}`}>
              <SocialLink>
                <BsTwitter size="1.2rem" />
                Twitter
              </SocialLink>
            </a>
          )}
          {profile.githubUsername && (
            <a href={`https://github.com/${profile.githubUsername}`}>
              <SocialLink>
                <AiOutlineGithub size="1.2rem" />
                GitHub
              </SocialLink>
            </a>
          )}
          {profile.facebookUsername && (
            <a href={`https://facebook.com/${profile.facebookUsername}`}>
              <SocialLink>
                <FaFacebookSquare size="1.2rem" />
                Facebook
              </SocialLink>
            </a>
          )}
          {profile.instagramUsername && (
            <a href={`https://instagram.com/${profile.instagramUsername}`}>
              <SocialLink>
                <FaInstagram size="1.2rem" />
                Instagram
              </SocialLink>
            </a>
          )}
        </ul>
      </div>
    )
  );
};

export default ProfileSocial;
