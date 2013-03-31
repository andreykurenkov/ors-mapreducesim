%% gaussWidth nodeLocalPct rackLocalPct remotePct
numNodes = 30;
data = [1.0, 3.3333333333333335, 31.666666666666664, 65.0; ...
1.58, 0.0, 32.758620689655174, 67.24137931034483; ...
2.16, 0.0, 35.0, 65.0; ...
2.74, 0.0, 40.32258064516129, 59.67741935483871; ...
3.32, 0.0, 41.66666666666667, 58.333333333333336; ...
3.9, 0.0, 46.666666666666664, 53.333333333333336; ...
4.48, 0.0, 55.00000000000001, 45.0; ...
5.06, 1.6129032258064515, 62.903225806451616, 35.483870967741936; ...
5.64, 1.6666666666666667, 65.0, 33.33333333333333; ...
6.22, 3.225806451612903, 70.96774193548387, 25.806451612903224; ...
6.8, 3.4482758620689653, 68.96551724137932, 27.586206896551722; ...
7.38, 5.357142857142857, 67.85714285714286, 26.785714285714285; ...
7.96, 8.620689655172415, 68.96551724137932, 22.413793103448278; ...
8.54, 8.620689655172415, 72.41379310344827, 18.96551724137931; ...
9.12, 8.928571428571429, 71.42857142857143, 19.642857142857142; ...
9.7, 10.0, 66.0, 24.0; ...
10.28, 10.0, 66.0, 24.0; ...
10.86, 10.0, 66.0, 24.0; ...
11.44, 10.416666666666668, 60.416666666666664, 29.166666666666668; ...
12.02, 10.416666666666668, 60.416666666666664, 29.166666666666668; ...
12.6, 10.416666666666668, 60.416666666666664, 29.166666666666668; ...
13.18, 8.695652173913043, 56.52173913043478, 34.78260869565217; ...
13.76, 9.090909090909092, 52.27272727272727, 38.63636363636363; ...
14.34, 9.090909090909092, 52.27272727272727, 38.63636363636363; ...
14.92, 10.0, 50.0, 40.0; ...
15.5, 10.526315789473683, 47.368421052631575, 42.10526315789473; ...
16.08, 13.333333333333334, 46.666666666666664, 40.0; ...
16.66, 13.333333333333334, 46.666666666666664, 40.0; ...
17.24, 13.333333333333334, 46.666666666666664, 40.0; ...
17.82, 13.333333333333334, 46.666666666666664, 40.0; ...
18.4, 13.333333333333334, 46.666666666666664, 40.0; ...
18.98, 13.333333333333334, 46.666666666666664, 40.0; ...
19.56, 13.333333333333334, 46.666666666666664, 40.0; ...
20.14, 13.333333333333334, 46.666666666666664, 40.0; ...
20.72, 13.333333333333334, 46.666666666666664, 40.0; ...
21.3, 13.333333333333334, 46.666666666666664, 40.0; ...
21.88, 13.333333333333334, 46.666666666666664, 40.0; ...
22.46, 13.333333333333334, 46.666666666666664, 40.0; ...
23.04, 13.333333333333334, 46.666666666666664, 40.0; ...
23.62, 13.333333333333334, 46.666666666666664, 40.0; ...
24.2, 13.333333333333334, 46.666666666666664, 40.0; ...
24.78, 13.333333333333334, 46.666666666666664, 40.0; ...
25.36, 13.333333333333334, 46.666666666666664, 40.0; ...
25.94, 13.333333333333334, 46.666666666666664, 40.0; ...
26.52, 13.333333333333334, 46.666666666666664, 40.0; ...
27.1, 13.333333333333334, 46.666666666666664, 40.0; ...
27.68, 13.333333333333334, 46.666666666666664, 40.0; ...
28.26, 13.333333333333334, 46.666666666666664, 40.0; ...
28.84, 13.333333333333334, 46.666666666666664, 40.0; ...
29.42, 13.333333333333334, 46.666666666666664, 40.0; ...
];

sigma = data(:,1);
nodeLocalPct = data(:,2);
rackLocalPct = data(:,3);
remotePct = data(:,4);

plot(sigma,nodeLocalPct,'-sb');
hold on;
plot(sigma,rackLocalPct,'-dr');
plot(sigma,remotePct,'-*k');
xlabel('sigma');
ylabel('% scheduling decisions');
legend('nodeLocal','rackLocal','remote');
hold off;