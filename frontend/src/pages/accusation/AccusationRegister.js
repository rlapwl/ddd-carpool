import { useEffect, useState } from 'react';

// material-ui
import {
    Button,
    Divider,
    FormHelperText,
    Grid,
    InputLabel,
    OutlinedInput,
    Stack,
    Typography,
    TextField,
    Card,
    CardContent
} from '@mui/material';

// third party
import * as Yup from 'yup';
import { Formik } from 'formik';

// project import
import AnimateButton from 'components/@extended/AnimateButton';
import { strengthColor, strengthIndicator } from 'utils/password-strength';

const Accusation = () => {
    const [level, setLevel] = useState();

    // const party = {};
    // party.start = '정자역';
    // party.end = '사당역';
    // party.staredDateTime = '2022-07-14 18:30';
    // party.memberId = '101';
    // party.memberName = '홍길동';
    // party.memberFormat = party.memberName + ' (' + party.memberId + ')';

    const changePassword = (value) => {
        const temp = strengthIndicator(value);
        setLevel(strengthColor(temp));
    };

    useEffect(() => {
        changePassword('');
    }, []);

    return (
        <>
            <Card sx={{ maxWidth: 9/10, mx: "auto" }}>
                <CardContent>
                    {/* <Stack justifyContent="center" alignItems="center" sx={{ my: 3 }}>
                        <Typography variant="h3">신고 등록</Typography>
                    </Stack>
                    <Divider /> */}
                    <Formik
                        initialValues={{
                            start: '정자역',
                            end: '사당역',
                            staredDateTime: '2022-07-14 18:30',
                            memberId: '101',
                            memberName: '홍길동',
                            memberFormat: '홍길동 (101)',
                            title: '',
                            desc: '',
                            submit: null
                        }}
                        validationSchema={Yup.object().shape({
                            title: Yup.string().max(255).required('Title is required')
                        })}
                        onSubmit={async (values, { setErrors, setStatus, setSubmitting }) => {
                            try {
                                setStatus({ success: false });
                                setSubmitting(false);
                            } catch (err) {
                                console.error(err);
                                setStatus({ success: false });
                                setErrors({ submit: err.message });
                                setSubmitting(false);
                            }
                        }}
                    >
                        {({ errors, handleBlur, handleChange, handleSubmit, isSubmitting, touched, values }) => (
                            <form noValidate onSubmit={handleSubmit}>
                                <Grid container spacing={3}>
                                    <Grid item xs={4}>
                                        <Stack spacing={1}>
                                        <InputLabel htmlFor="firstname-signup">출발지</InputLabel>
                                            <TextField
                                                id="outlined-read-only-input"
                                                value={values.start}
                                                InputProps={{
                                                    readOnly: true
                                                }}
                                            />
                                        </Stack>
                                    </Grid>
                                    <Grid item xs={4}>
                                        <Stack spacing={1}>
                                        <InputLabel htmlFor="firstname-signup">도착지</InputLabel>
                                            <TextField
                                                id="outlined-read-only-input"
                                                value={values.end}
                                                InputProps={{
                                                    readOnly: true
                                                }}
                                            />
                                        </Stack>
                                    </Grid>
                                    <Grid item xs={4}>
                                        <Stack spacing={1}>
                                        <InputLabel htmlFor="firstname-signup">출발 시간</InputLabel>
                                            <TextField
                                                id="outlined-read-only-input"
                                                value={values.staredDateTime}
                                                InputProps={{
                                                    readOnly: true
                                                }}
                                            />
                                        </Stack>
                                    </Grid>
                                    <Grid item xs={4}>
                                        <Stack spacing={1}>
                                            <InputLabel htmlFor="firstname-signup">신고 대상</InputLabel>
                                            <TextField
                                                id="outlined-read-only-input"
                                                value={values.memberFormat}
                                                InputProps={{
                                                    readOnly: true
                                                }}
                                            />
                                        </Stack>
                                    </Grid>
                                    <Grid item xs={12}>
                                        <Stack spacing={1}>
                                            <InputLabel htmlFor="title-signup">제목</InputLabel>
                                            <OutlinedInput
                                                id="title-login"
                                                type="title"
                                                value={values.title}
                                                name="title"
                                                onBlur={handleBlur}
                                                onChange={handleChange}
                                                placeholder="Title"
                                                fullWidth
                                                error={Boolean(touched.title && errors.title)}
                                            />
                                            {touched.title && errors.title && (
                                                <FormHelperText error id="helper-text-title-signup">
                                                    {errors.title}
                                                </FormHelperText>
                                            )}
                                        </Stack>
                                    </Grid>
                                    <Grid item xs={12}>
                                        <Stack spacing={1}>
                                            <InputLabel htmlFor="desc-signup"> 신고 내용</InputLabel>
                                            <OutlinedInput
                                                fullWidth
                                                error={Boolean(touched.desc && errors.desc)}
                                                id="desc-signup"
                                                value={values.desc}
                                                name="desc"
                                                onBlur={handleBlur}
                                                onChange={handleChange}
                                                inputProps={{}}
                                            />
                                            {touched.desc && errors.desc && (
                                                <FormHelperText error id="helper-text-desc-signup">
                                                    {errors.desc}
                                                </FormHelperText>
                                            )}
                                        </Stack>
                                    </Grid>
                                    <Grid item xs={12}>
                                        <AnimateButton>
                                            <Button
                                                disableElevation
                                                disabled={isSubmitting}
                                                fullWidth
                                                size="large"
                                                type="submit"
                                                variant="contained"
                                                color="primary"
                                            >
                                                등록
                                            </Button>
                                        </AnimateButton>
                                    </Grid>
                                </Grid>
                            </form>
                        )}
                    </Formik>
                </CardContent>
            </Card>
        </>
    );
};

export default Accusation;
