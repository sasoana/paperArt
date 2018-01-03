import React from 'react';
import { StackNavigator } from 'react-navigation';
import ModelList from './ModelList'
import MainScreen from './MainScreen'
import ModelDetails from "./ModelDetails";
import CategoryList from "./CategoryList"
import ModelDetailsAdd from "./ModelDetailsAdd";

const RootNavigator = StackNavigator({
    Index: { screen: MainScreen },
    Categories: {screen: CategoryList},
    List: { screen: ModelList },
    Details: { screen: ModelDetails },
    AddItem: {screen: ModelDetailsAdd },

});

export default class App extends React.Component<{}> {
    render() {
        return <RootNavigator/>
    }
}