import moment from 'moment';
import Button from '../elements/Button/Button';
import ProfileApi from '../../api/profile.api';
import Link from 'next/link';
import { toast } from 'react-toastify';
import useAuth from '../../hooks/useAuth';
import { useRouter } from 'next/router';
import useActiveProfile from '../../hooks/useActiveProfile';
import { useState } from 'react';
import Image from 'next/image';

const EditMyProfileButton = () => {
  return (
    <Link href="/edit/profile">
      <a className="bg-transparent hover:bg-gray-200 text-gray-600 border rounded py-3 px-4 text-sm text-gray-500">
        Edit my profile
      </a>
    </Link>
  );
};

const UnfollowButton = () => {
  const { username, reload } = useActiveProfile();

  const unfollowUser = () => {
    ProfileApi.unfollowUser({ username }).then((res) => {
      toast(res.data.message, { type: 'success' });
      reload();
    });
  };

  return (
    <Button
      className="bg-green-500 hover:bg-red-500 text-white rounded transition whitespace-nowrap px-5 py-2"
      onClick={unfollowUser}
    >
      Following
    </Button>
  );
};

const FollowButton = () => {
  const router = useRouter();
  const { authenticated } = useAuth();
  const { username, reload } = useActiveProfile();

  const followUser = () => {
    if (!authenticated) {
      router.push('/login');
      return;
    }
    ProfileApi.followUser({ username })
      .then((res) => {
        toast(res.data.message, {
          type: 'success',
        });
        reload();
      })
      .catch((err) => {
        toast(err.response.data.message, { type: 'error' });
      });
  };

  return (
    <Button appearance="secondary" size="lg" onClick={followUser}>
      Follow
    </Button>
  );
};

function CancelFollowRequestButton() {
  const { username, reload } = useActiveProfile();
  const [buttonText, setButtonText] = useState('Pending');

  const unfollowUser = () => {
    ProfileApi.unfollowUser({ username }).then((res) => {
      toast(res.data.message, { type: 'success' });
      reload();
    });
  };

  return (
    <Button
      className="bg-yellow-500 hover:bg-red-500 text-white text-center rounded transition whitespace-nowrap px-5 py-2 w-24"
      onClick={unfollowUser}
      onMouseEnter={() => setButtonText('Cancel')}
      onMouseLeave={() => setButtonText('Pending')}
    >
      {buttonText}
    </Button>
  );
}

function ProfileSummary({ type }) {
  const { profile } = useActiveProfile();

  if (!profile) {
    return (
      <div className="px-4 bg-gray-100">
        <div className="mx-auto flex flex-col sm:flex-row items-center sm:items-stretch justify-center sm:justify-between w-full xl:w-4/5 max-w-screen-xl flex-nowrap py-8 gap-7">
          <div className="rounded-full h-full h-[120px] w-[120px]" />
          <div className="flex flex-col w-full gap-4">
            <div className="bg-gray-50 bg-gradient-to-r from-gray-50 to-gray-200 w-1/3 h-6 rounded" />
            <div className="bg-gray-50 bg-gradient-to-r from-gray-50 to-gray-200 w-1/2 h-6 rounded" />
            <div className="bg-gray-50 bg-gradient-to-r from-gray-50 to-gray-200 w-1/2 h-6 rounded" />
          </div>
        </div>
      </div>
    );
  }

  const selfProfile = profile.canEdit;
  const following = !selfProfile && profile.following;
  const notFollowing =
    !selfProfile && !profile.following && !profile.hasFollowingRequest;
  const hasFollowingRequest = !selfProfile && profile.hasFollowingRequest;

  return (
    <div className="px-4 bg-gray-100">
      <div className="mx-auto flex flex-col sm:flex-row items-center sm:items-stretch justify-center sm:justify-between w-full xl:w-4/5 max-w-screen-xl flex-nowrap py-8 gap-7">
        <Image
          src="https://i.pravatar.cc/300"
          alt="profile picture"
          height="120px"
          width="120px"
          className="rounded-full h-full"
        />
        <div className="flex flex-col sm:flex-row w-full justify-between items-center sm:items-stretch gap-4 sm:gap-0">
          <div className="flex flex-col justify-center items-center sm:items-stretch gap-2">
            <div className="flex items-baseline gap-2 text-gray-600">
              <span className="text-2xl font-semibold">{profile.name}</span>
              <span className="font-sans text-sm">@{profile.username}</span>
            </div>
            <div className="text-gray-400 text-sm mb-3">{profile.about}</div>
            <div className="text-gray-400 text-sm">
              Joined {moment(profile.registrationDate).fromNow()}
            </div>
          </div>
          <div className="flex flex-col justify-between items-center sm:items-end gap-5 sm:gap-1">
            {selfProfile && <EditMyProfileButton />}
            {following && <UnfollowButton />}
            {hasFollowingRequest && <CancelFollowRequestButton />}
            {notFollowing && <FollowButton />}
            <div className="flex gap-4">
              <div
                className={`flex flex-col items-center border-b-2 ${
                  type === 'activities'
                    ? 'border-yellow-400'
                    : 'border-transparent'
                } hover:border-yellow-400 cursor-pointer select-none`}
              >
                <div className="text-xs text-gray-400">ACTIVITIES</div>
                <div className="text-gray-600">{profile.activityCount}</div>
              </div>
              <Link href={`/@${profile.username}/followers`}>
                <a>
                  <div
                    className={`flex flex-col items-center border-b-2 ${
                      type === 'followers'
                        ? 'border-yellow-400'
                        : 'border-transparent'
                    } hover:border-yellow-400 cursor-pointer select-none`}
                  >
                    <div className="text-xs text-gray-400">FOLLOWERS</div>
                    <div className="text-gray-600">
                      {profile.followersCount}
                    </div>
                  </div>
                </a>
              </Link>
              <Link href={`/@${profile.username}/following`}>
                <a>
                  <div
                    className={`flex flex-col items-center border-b-2 ${
                      type === 'following'
                        ? 'border-yellow-400'
                        : 'border-transparent'
                    } hover:border-yellow-400 cursor-pointer select-none`}
                  >
                    <div className="text-xs text-gray-400">FOLLOWING</div>
                    <div className="text-gray-600">
                      {profile.followingCount}
                    </div>
                  </div>
                </a>
              </Link>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
}

export default ProfileSummary;
