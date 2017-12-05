import React from 'react';
import { StackNavigator } from 'react-navigation';
import ModelList from './ModelList'
import MainScreen from './MainScreen'
import ModelDetails from "./ModelDetails";

const RootNavigator = StackNavigator({
    Index: { screen: MainScreen },
    List: { screen: ModelList },
    Details: { screen: ModelDetails }

});

export default class App extends React.Component<{}> {
    render() {
        return <RootNavigator/>
    }
}