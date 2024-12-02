import Category from "../../components/category/Category";
import HeroSection from "../../components/heroSection/HeroSection";
import HomePageProductCard from "../../components/homePageProductCard/HomePageProductCard";
import Layout from "../../components/layout/Layout";
import {Container} from "react-bootstrap";

const HomePage = () => {
    return (
        <Layout>
            <HeroSection/>
            <Container className="my-5">
                <Category/>
                <HomePageProductCard/>
            </Container>
        </Layout>
    );
};

export default HomePage;
