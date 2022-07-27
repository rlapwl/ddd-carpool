// assets
import {
    AppstoreAddOutlined,
    AntDesignOutlined,
    BarcodeOutlined,
    BgColorsOutlined,
    FontSizeOutlined,
    LoadingOutlined,
    ScheduleOutlined
} from '@ant-design/icons';

// icons
const icons = {
    FontSizeOutlined,
    BgColorsOutlined,
    BarcodeOutlined,
    AntDesignOutlined,
    LoadingOutlined,
    AppstoreAddOutlined,
    ScheduleOutlined
};

// ==============================|| MENU ITEMS - UTILITIES ||============================== //

const utilities = {
    id: 'utilities',
    title: 'Utilities',
    type: 'group',
    children: [
        {
            id: 'util-typography',
            title: 'Typography',
            type: 'item',
            url: '/typography',
            icon: icons.FontSizeOutlined
        },
        {
            id: 'util-color',
            title: 'Color',
            type: 'item',
            url: '/color',
            icon: icons.BgColorsOutlined
        },
        {
            id: 'util-shadow',
            title: 'Shadow',
            type: 'item',
            url: '/shadow',
            icon: icons.BarcodeOutlined
        },
        {
            id: 'ant-icons',
            title: 'Ant Icons',
            type: 'item',
            url: '/icons/ant',
            icon: icons.AntDesignOutlined,
            breadcrumbs: false
        },
        {
            id: 'now-carpool ',
            title: '진행중인 카풀',
            type: 'item',
            url: '/icons/ant',
            icon: icons.ScheduleOutlined,
            breadcrumbs: false
        },
        {
            id: 'carpool-history',
            title: '내 카풀 내역',
            type: 'collapse',
            icon: icons.LoadingOutlined,
            children: [
                {
                    id: 'now-carpool ',
                    title: '진행중인 카풀',
                    type: 'item',
                    url: '/icons/ant',
                    breadcrumbs: false
                },
                {
                    id: 'last-carpool ',
                    title: '지난 카풀',
                    type: 'item',
                    url: '/party-matching',
                    breadcrumbs: false
                }
            ]
        },
        {
            id: 'carpool-accusation',
            title: '신고',
            type: 'collapse',
            icon: icons.LoadingOutlined,
            children: [
                {
                    id: 'accusation-register ',
                    title: '신고 등록',
                    type: 'item',
                    url: '/accusation/register',
                    breadcrumbs: false
                }
            ]
        }
    ]
};

export default utilities;
