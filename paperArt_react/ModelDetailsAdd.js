import React from 'react';
import {Text, TextInput, Button, ScrollView} from "react-native";
import DatePicker from 'react-native-datepicker';

export default class ModelDetailsAdd extends React.Component {
    constructor(props) {
        super(props);
        const { params } = this.props.navigation.state;
        this.state = {
            id: params.data.id,
            categoryId: params.data.categoryId,
            name: params.data.name,
            paper: params.data.paper,
            color: params.data.color,
            duration: params.data.duration,
            createdAt: params.data.createdAt,
        };
    }

    render() {
        return (
            <ScrollView style={{paddingRight: 20, paddingLeft: 20}}>
                <Text>Model name:</Text>
                <TextInput
                    style={{borderColor: 'gray', borderWidth: 1, textAlign: 'center',
                        margin: 10}}
                    onChangeText={(text) => this.setState({name: text})}
                    value={this.state.name}
                    multiline={true}
                />
                <Text>Paper type:</Text>
                <TextInput
                    style={{borderColor: 'gray', borderWidth: 1, textAlign: 'center',
                        margin: 10}}
                    onChangeText={(text) => this.setState({paper: text})}
                    value={this.state.paper}
                />
                <Text>Color:</Text>
                <TextInput
                    style={{borderColor: 'gray', borderWidth: 1, textAlign: 'center', margin: 10}}
                    onChangeText={(text) => this.setState({color: text})}
                    value={this.state.color}
                />
                <Text>Duration:</Text>
                <TextInput
                    style={{borderColor: 'gray', borderWidth: 1, textAlign: 'center', margin: 10}}
                    keyboardType='numeric'
                    onChangeText={(text) => this.setState({duration: text})}
                    value={this.state.duration.toString()}
                />
                <Text>Created at:</Text>
                <DatePicker
                    style={{borderColor: 'gray', borderWidth: 1, margin: 10}}
                    date={this.state.createdAt}
                    mode="date"
                    format="DD-MM-YYYY"
                    showIcon={false}
                    customStyles={{
                        dateInput: {
                            alignItems : 'flex-start',
                            padding:5
                        },
                    }}
                    onDateChange={(date_in) => {this.setState({createdAt: date_in});}}
                />
                <Button
                    onPress={()=> {this.props.navigation.state.params.saveItem(this.state.id, this.state.categoryId, this.state.name,
                        this.state.paper, this.state.color, this.state.duration, this.state.createdAt);
                        this.props.navigation.goBack();}}
                    title="Save"
                />

            </ScrollView>
        )
    }
}