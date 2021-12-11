import React from 'react';
import DefaultLayout from "../../app/components/layouts/DefaultLayout/DefaultLayout";
import SearchPage from "../../app/components/templates/SearchPage";


const Search = () => (
    <SearchPage/>
);

Search.getLayout = page => DefaultLayout(page)

export default Search;