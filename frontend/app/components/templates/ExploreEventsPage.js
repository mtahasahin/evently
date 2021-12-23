import React from 'react';
import moment from 'moment-timezone';
import useAuth from '../../hooks/useAuth';
import languages from '../../constants/language-list';
import SelectSearch, { fuzzySearch } from 'react-select-search-nextjs';
import AxiosInstance from '../../api/AxiosInstance';
import EventCardList from '../modules/EventCardList';

const languageOptions = languages.map((e) => ({
  name: `${e.name} (${e.nativeName})`,
  value: e.code,
}));

const SelectLanguage = ({ searchData, setSearchData }) => {
  const setLanguage = (language) => {
    setSearchData((prev) => ({
      ...prev,
      language: language,
    }));
  };
  return (
    <div className="ml-2 text-lg">
      <span className="text-black font-normal border-b border-yellow-500"></span>
      <SelectSearch
        options={languageOptions}
        search
        filterOptions={fuzzySearch}
        value={searchData.language}
        onChange={setLanguage}
        name="language"
      ></SelectSearch>
    </div>
  );
};

const ExploreEventsPage = () => {
  const [searchData, setSearchData] = React.useState({
    page: 0,
    language: 'en',
    results: [],
  });

  const { user } = useAuth();
  const timezone = user?.profile?.timezone ?? moment.tz.guess();

  React.useEffect(() => {
    AxiosInstance.post(`/upcoming`, {
      page: searchData.page,
      language: searchData.language,
      limit: 25,
    }).then((res) => {
      setSearchData((prev) => ({
        ...prev,
        results: res.data.data.events,
      }));
    });
  }, [searchData.language]);

  return (
    <div className="w-full px-5 my-2">
      <div className="flex items-baseline gap-1 text-gray-400 text-2xl font-thin pb-8 px-2">
        <span>I'm looking for events in</span>
        <SelectLanguage searchData={searchData} setSearchData={setSearchData} />
      </div>
      <EventCardList events={searchData.results} timezone={timezone} />
    </div>
  );
};

export default ExploreEventsPage;
