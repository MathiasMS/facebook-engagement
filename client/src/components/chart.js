import React, { Component } from 'react';
import Chart, { Line } from 'react-chartjs-2';

export default class FacebookChart extends Component {
    constructor (props) {
        super(props);

        this.data = {
            labels: ["0hs", "1hs", "2hs", "3hs", "4hs", "5hs", "6hs", "7hs", "8hs", "9hs", "10hs", "11hs", "12hs", "13hs", "14hs", "15hs", "16hs", "17hs", "18hs", "19hs", "20hs", "21hs", "22hs", "23hs" ],
            datasets: [
                {
                    label: "Likes",
                    fill: false,
                    borderColor: '#EC932F',
                    backgroundColor: '#EC932F',
                    pointBorderColor: '#EC932F',
                    pointBackgroundColor: '#EC932F',
                    pointHoverBackgroundColor: '#EC932F',
                    pointHoverBorderColor: '#EC932F',
                    yAxisID: 'y-axis-2',
                    data: this.props.data.likes,
                },
                {
                    label: "Shares",
                    fill: false,
                    backgroundColor: '#71B37C',
                    borderColor: '#71B37C',
                    hoverBackgroundColor: '#71B37C',
                    hoverBorderColor: '#71B37C',
                    yAxisID: 'y-axis-1',
                    data: this.props.data.shares,
                },
            ]
        };

        this.options = {
            responsive: true,
            tooltips: {
                mode: 'label'
            },
            elements: {
                line: {
                    fill: false
                }
            },
            scales: {
                xAxes: [
                    {
                        display: true,
                        gridLines: {
                            display: false
                        },
                        labels: {
                            show: true
                        }
                    }
                ],
                yAxes: [
                    {
                        type: 'linear',
                        display: true,
                        position: 'left',
                        id: 'y-axis-1',
                        gridLines: {
                            display: false
                        },
                        labels: {
                            show: true
                        }
                    },
                    {
                        type: 'linear',
                        display: true,
                        position: 'right',
                        id: 'y-axis-2',
                        gridLines: {
                            display: false
                        },
                        labels: {
                            show: true
                        }
                    }
                ]
            }
        };
    }


    render() {
        return (
            <div>
                <Line options={this.options} data={this.data} />
            </div>
        )
    }
}
